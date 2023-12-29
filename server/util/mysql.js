require('dotenv').config();

const mysql = require('mysql');
const { async } = require('rxjs');

class MySQL{

    constructor() {
        this.db = null;
    }

    static getInstance() {
        if (!this.instance) {
          this.instance = new MySQL();
        }
        return this.instance;
    }

    setConnection(){
        this.db = mysql.createConnection({
            host : process.env.HOST,
            user : process.env.USER,
            password : process.env.PASS,
            database : process.env.DB
        });
    }

   
    async select(field,model,callback){
        let qry = "SELECT "+field+" FROM " + model;
        //console.log(qry)
        this.db.query(qry,(error,result,field)=>{
            return callback(error ? {
                status : "error",
                message : error
            }
                 : result);
        });
    }
    
    async insert(table,data,callback){
        this.db.query("INSERT INTO "+table+ " SET ?",data,(error,result,field)=>{
            return callback(error ?
                {
                    "status" : "error",
                    "message" : error,
                    "data" : null
                }:
                {
                    "status" : "sukses",
                    "message" : "berhasil menambahkan data !",
                    "data" : result
                });
        });
    }
    
    async remove(table,data,callback){
        this.db.query("DELETE FROM "+table+ " WHERE ?",data,(error,result,field)=>{
            return callback(error ? 
                {
                    "status" : "error",
                    "message" : error,
                    "data" : null
                }:
                {
                    "status" : "sukses",
                    "message" : "berhasil menghapus data !",
                    "data" : null
                });
            
        });
    }
    async update(table,data,condition,callback){
        this.db.query("UPDATE "+table+ " SET ? WHERE ?",[data,condition],(error,result,field)=>{
            return callback(error ?
                {
                    "status" : "error",
                    "message" : error,
                    "data" : null
                }:
                {
                    "status" : "sukses",
                    "message" : "berhasil mengupdate data !",
                    "data" : result
                });
        });
    }
}


module.exports = MySQL;
