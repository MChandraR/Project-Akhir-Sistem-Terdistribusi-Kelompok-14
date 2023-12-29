const user = require('../model/user'); 
const { update } = require('../util/mysql');
let util = require('../util/util');
util = new util();
const users = new user();
require('dotenv').config();

const {
   PutObjectCommand, CopyObjectCommand
} = require('@aws-sdk/client-s3');
const fs = require("fs")
const sessionController = require("./sessionController");
const sC = new sessionController();

class usersController {
    getAllUser(res){
        users.get((result)=>{res.send(result)});
    }

    signIn(res,data,req){
        var ip = req.headers['x-forwarded-for'] || req.socket.remoteAddress ;
        if(data['username']==null || data['password']==null){
            res.send({
                "status" : "gagal",
                "message" : "invalid request"
            });
        }

        console.log(data['username'],data['password']);
        let usersData = users.
        customSelectQuery(" WHERE ( BINARY username='"+data['username']+"' OR BINARY email='"+data['username']+"') AND BINARY password = '"+data['password']+"' LIMIT 1").
        get((result)=>{
            let resData = JSON.parse(JSON.stringify(result))[0];
            console.log("data" , resData);
            if(result.status == "error"){
                util.sendResponse(res,"gagal","internal server error !");
            }else if(result.length < 1){
                util.sendResponse(res,"gagal","Username atau password salah !");
            }else{
                sC.addSession(resData.user_id);
                console.log("User " + data['username'] + " telah login !\nIP : " + ip + "\n");
                util.sendResponse(res,"sukses","Berhasil login !",resData)
            }
        });

    }

    signUp(res,data,s3){
        if(data['username']==null || data['password']==null || data['email'] == null){
            util.sendResponse(res,"gagal","Invalid request");
            return;
        }
        users.select(" SUBSTR(user_id,2) as newID ").customSelectQuery(" ORDER BY user_id DESC LIMIT 1").get((result)=>{
            let newID = JSON.parse(JSON.stringify(result))[0].newID;
            newID = "000000"+(parseInt(newID)+1).toString();
            newID = "U" + newID.slice(-6);
            users.create({
                "user_id" : newID,
                "username" : data['username'],
                "password" : data['password'],
                "email" : data['email'],
                "role" : "user"
            },async (result)=>{

                try {                
                    const params = {
                      Bucket: process.env.BUCKET,
                      Key: newID+".png", // Specify the desired key in S3
                      CopySource : '/'+process.env.BUCKET+'/base.png',
                    };
                
                    const command = new CopyObjectCommand(params);
                    const result = await s3.send(command);
                
                    console.log(`File ${newID+".png"} uploaded successfully: ${result.Location}`);
                  } catch (error) {
                    console.error('Error uploading file:', error);
                  }

                res.send(
                   result
                );
            });    
            
        });
       
    }

    updateProfile(res,data){
        let updateData={};
        if(data['username']!="")updateData["username"] = data['username'];
        if(data['updatePass'])updateData["password"] = data['password'];
        if(data['email']!="") updateData["email"] = data['email'];
        users.where("user_id",data['user_id']).update(
            updateData
        ,(result)=>{
            if(result.status=="sukses"){
                console.log(updateData);
                util.sendResponse(res,"sukses","Berhasil mengupdate user profile !",updateData);
            }else{
                util.sendResponse(res,"error","Gagal mengupdate profile!");
            }
        });
    }
}

module.exports = usersController;