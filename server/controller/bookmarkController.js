const model = require('../model/bookmark'); 
const bookmark = new model();
const beritaController = require('./beritaController');
const bC = new beritaController();
const util = require('../util/util');
const utilS = new util();

class bookmarkController {
   addBookmark(res,data){
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
        bC.addBerita(data,(result)=>{
            if(result.status =="sukses"){
                bookmark.create({
                    "berita_id" : result.newID,
                    "user_id" : data['user_id'],
                    "waktu" : waktuFormatted
                },(response)=>{
                    utilS.sendResponse(res,response.status,response.message);
                });
               
            }else{
                utilS.sendResponse(res,result.status,result.message);
            }
        });
   }

   getUserBookmark(res,data){
        bookmark.customSelectQuery(" INNER JOIN berita ON bookmark.berita_id = berita.berita_id WHERE bookmark.user_id = '"+data['user_id']+"'").get((result)=>{
            if(result.status != null){
                res.send(result);
            }else{
                utilS.sendResponse(res,"sukses","berhasil mendapatkan data bookmark!",result);
            }
            
        });
   }

   removeUserBookmark(res,data){
        bookmark.where('mark_id',data['mark_id']).delete((result)=>{
            res.send(result);
        });
   }

   searchUserBookmark(res,data){
        bookmark.customSelectQuery(" INNER JOIN berita ON bookmark.berita_id = berita.berita_id WHERE user_id LIKE '%"+data['user_id']+"%' AND berita.judul LIKE '%"+data['keyword']+"%';").get((result)=>{
            res.send({
                "status" : "berhasil",
                "message" : "berhasil mengambil data !",
                "data" : result
            });
        });
   }
}

module.exports = bookmarkController;