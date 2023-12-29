const session = require('../model/session'); 
const util = require('../util/util');
const sessions = new session();
const utils = new util();

class sessionController {
    addSession(user_id){
        //console.log(user_id);
        sessions.customSelectQuery(" WHERE user_id = '"+user_id+"' LIMIT 1").get((result)=>{
            result = JSON.parse(JSON.stringify(result));
            //console.log(result);
            if(result.status == null){
                
                console.log(result);
                sessions.select(" SUBSTR(session_id,2) AS newID ").customSelectQuery(" ORDER BY session_id DESC LIMIT 1 ").get((res)=>{
                    let newID = parseInt(res.length>0 ? JSON.parse(JSON.stringify(res))[0].newID : "000000")+1;
                    res = JSON.parse(JSON.stringify(res))[0];
                   // console.log(res,newID);
                    const waktuSekarang = new Date();

                    // Mendapatkan komponen waktu
                    const tahun = waktuSekarang.getFullYear();
                    const bulan = String(waktuSekarang.getMonth() + 1).padStart(2, '0'); // Perlu ditambah 1 karena indeks bulan dimulai dari 0
                    const hari = String(waktuSekarang.getDate()).padStart(2, '0');
                    const jam = String(waktuSekarang.getHours()).padStart(2, '0');
                    const menit = String(waktuSekarang.getMinutes()).padStart(2, '0');
                    const detik = String(waktuSekarang.getSeconds()).padStart(2, '0');
                
                    // Membuat string dengan format yang diinginkan
                    const waktuFormatted = `${tahun}-${bulan}-${hari} ${jam}:${menit}:${detik}`;
                    if(result.length < 1){
                        result = result[0];
                        sessions.create({
                            "session_id" : "S"+("000000"+newID.toString()).slice(-6),
                            "user_id" : user_id,
                            "created_at" : waktuFormatted,
                            "token" : utils.createToken()
                        },(result)=>{
                            //console.log(result);
                        });
                    }else{
                        sessions.where("user_id",user_id).update({
                            "token" : utils.createToken()
                        },(msg)=>{
                            //console.log(msg);
                        });
                    }
                });
               
            }else{
                console.log("Gagal membuat session !", result.message);
            }
        });
    }
}

module.exports = sessionController;
