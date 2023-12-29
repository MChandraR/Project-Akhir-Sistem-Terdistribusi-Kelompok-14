const model = require('../model/model'); 
const models = new model();
const fs = require('fs');

class resourceController {
    uploadProfilePicture(res,body,file){
        console.log("Profile Picture Update\nFile info :");
        console.log({
            "filename" : file.originalname,
            "tmp_path" : file.filename,
            "size" : (file.size/1000).toString()+" kb"
        });
        // fs.copyFile("./"+file.path,"./public/img/users/profile/"+file.originalname,(res)=>{
        //     if(res!=null)console.log(res);
        // });
        // fs.unlink("./"+file.path,(message)=>{
        //     console.log(message);
        // });
        res.send({
            "filename" : file.originalname,
            "tmp_path" : file.filename,
            "size" : (file.size/1000).toString()+" kb"
        });
    }
}

module.exports = resourceController;