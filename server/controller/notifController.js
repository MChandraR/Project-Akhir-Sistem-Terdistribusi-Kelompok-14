const model = require('../model/model'); 
const models = new model();

let systemNotif = []

class notifController {
    constructor(){
        systemNotif.push({
            "notifID" : "N000001",
            "message" : "Selamat datang diaplikasi mNews !"
        });
        systemNotif.push({
            "notifID" : "N000002",
            "message" : "Jika terdapat bug/crash laporkan kepada admin atau developer  !"
        });
        systemNotif.push({
            "notifID" : "N000003",
            "message" : "Berikan izin penyimpanan untuk dapat menggunakan fitur update foto !"
        });
    }

    getNotif(res){
        res.send(systemNotif);
    }
   
}

module.exports = notifController;