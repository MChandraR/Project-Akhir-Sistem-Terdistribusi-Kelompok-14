const berita = require('../model/berita'); 
const beritas = new berita();
const axios = require('axios');
const utl = require('../util/util');
const util = new utl();

let dataBerita = []
let kategori = {
    "terbaru" : {
        "url" : "https://api-berita-indonesia.vercel.app/cnbc/terbaru/",
        "data" : [],
        "hasadded" : {}
        },
    "politik" :{ 
        "url" : "https://api-berita-indonesia.vercel.app/antara/politik/",
        "data" : [],
        "hasadded" : {}
        },
    "tech" : {
        "url" : "https://api-berita-indonesia.vercel.app/cnbc/tech/",
        "data" : [],
        "hasadded" : {}
        },
    "lifestyle" : {
        "url" : "https://api-berita-indonesia.vercel.app/cnbc/lifestyle/",
        "data" : [],
        "hasadded" : {}
        }
};



let judul={}

let APIsource = [
    //"http://api.mediastack.com/v1/news?access_key=3a799119da9e33cd4f9b2f29671f7f72&keywords=&countries=id",
    "https://api-berita-indonesia.vercel.app/antara/terbaru/",
    "https://api-berita-indonesia.vercel.app/cnbc/terbaru/",
    "https://newsapi.org/v2/top-headlines?country=id&apiKey=6935046771544a44aa3128676581d820"

];

class beritaController {
    async fetchBerita(url,tag){
        let idx = 0
        const apiURL = url;
        axios.get(apiURL)
        .then(function (response) {
            var resData = response.data;
            
                if(resData.articles!=null){
                    for(let i=0;i<resData.articles.length;i++){
                        if(judul[resData.articles[i].title]) continue
                        dataBerita.push({
                            "kategori" : tag,
                            "src" : 1,
                            "judul" : resData.articles[i].title,
                            "deskripsi" : resData.articles[i].description,
                            "url" : resData.articles[i].url,
                            "author" : resData.articles[i].author,
                            "gambar" : resData.articles[i].urlToImage,
                            "waktu" : util.encodeTime(resData.articles[i].publishedAt),
                            "sumber" : resData.articles[i].source.name
                        })
                        if(kategori[tag]!=null && !kategori[tag].hasadded[resData.articles[i].title]){
                            kategori[tag].data.push({
                                "kategori" : tag,
                                "src" : 1,
                                "judul" : resData.articles[i].title,
                                "deskripsi" : resData.articles[i].description,
                                "url" : resData.articles[i].url,
                                "author" : resData.articles[i].author,
                                "gambar" : resData.articles[i].urlToImage,
                                "waktu" : util.encodeTime(resData.articles[i].publishedAt),
                                "sumber" : resData.articles[i].source.name
                            });
                            kategori[tag].hasadded[resData.articles[i].title] = 1;
                        }
                        judul[resData.articles[i].title] = 1;
                    }
                }else {
                    if(resData.articles==null) resData = resData.data
                    for(let i=0;i<resData.posts.length;i++){
                        if(judul[resData.posts[i].title]) continue
                        if(resData.posts[i]==null)break;
                        dataBerita.push({
                            "kategori" : tag,
                            "src" : 2,
                            "judul" : resData.posts[i].title,
                            "deskripsi" : resData.posts[i].description,
                            "url" : resData.posts[i].link,
                            "author" : "-",
                            "gambar" : resData.posts[i].thumbnail,
                            "waktu" : util.encodeTime(resData.posts[i].pubDate),
                            "sumber" :"ANTARA News"
                        });
                        if(kategori[tag]!=null && !kategori[tag].hasadded[resData.posts[i].title]){
                            kategori[tag].data.push({
                                "kategori" : tag,
                                "src" : 2,
                                "judul" : resData.posts[i].title,
                                "deskripsi" : resData.posts[i].description,
                                "url" : resData.posts[i].link,
                                "author" : "-",
                                "gambar" : resData.posts[i].thumbnail,
                                "waktu" : util.encodeTime(resData.posts[i].pubDate),
                                "sumber" :"ANTARA News"
                            });
                            kategori[tag].hasadded[resData.posts[i].title] = 1;
                        }
                        judul[resData.posts[i].title] = 1;
                    }
                
                }
            
            
        }).catch((error)=>{
            console.log(error);
            return {
                "error" : error
            }
        });
    }

    searchBerita(res,data){
        console.log(data);
        console.log("mencari berita : "+data['keyword']);
        let resData = [];
        for(let i=0;i<dataBerita.length;i++){
            if(dataBerita[i].judul==null)continue;
            if(dataBerita[i].judul.toString().toLowerCase().includes(data['keyword'].toLowerCase())){
                resData.push(dataBerita[i]);
            }
        }

        res.send(resData);
    }

    async getBerita(res,req){
        console.log("Fetching !");
        const beritaPromises = APIsource.map(source => this.fetchBerita(source,"terbaru"));
        try {
            await Promise.all(beritaPromises);
            for(let tag in kategori){
                this.fetchBerita(kategori[tag].url,tag);
            }
            if(req.query.kategori == "" || req.query.kategori == null ||req.query.kategori.toLowerCase() == "terbaru"  ){
                if(req.query.user_id!=null || req.query.user_id != ""){
                    beritas.select(" kategori, COUNT(*) AS jumlah ").customSelectQuery(" INNER JOIN bookmark ON berita.berita_id = bookmark.berita_id WHERE user_id='"+req.query.user_id+"'  GROUP BY kategori " ).get((result)=>{
                        //console.log(result);
                        if(result.status==null){
                            result = JSON.parse(JSON.stringify(result));
                            for(let i=0;i<result.length;i++){
                                let targ = i;
                                for(let j=i+1;j<result.length;j++){
                                    if(result[j].jumlah>result[targ].jumlah) targ = j;
                                }
                                let temp = result[targ];
                                result[targ] = result[i];
                                result[i] = temp;
                            }
                            let resData = []
                            let sudah = {}
                            let total = 0
                            for(let i=0;i<result.length;i++)total+=result[i].jumlah;
                            for(let i=0;i<result.length;i++){
                                if(result[i].kategori!=null){
                                    if(kategori[result[i].kategori].data.length < 1) continue;
                                    for(let j=0;j<((result[i].jumlah/total)*100);j++){
                                        let idx = Math.floor(Math.random()*(kategori[result[i].kategori].data.length-1));
                                        let data = kategori[result[i].kategori].data[idx];
                                        if(sudah[data['judul']]==null){
                                            sudah[data['judul']] = 1;
                                            resData.push(kategori[result[i].kategori].data[idx]);
                                        }
                                    }
                                }
                            }
                            for(let i=0;i<dataBerita.length;i++){
                                let idx = Math.floor(Math.random()*(dataBerita.length-1));

                                if(sudah[dataBerita[idx].judul]==null){
                                    sudah[dataBerita[idx].judul] = 1;
                                    resData.push(dataBerita[idx]);
                                }
                            }
                            res.send(resData.length>2 ? resData :dataBerita);
                        }else{
                            res.send(dataBerita);
                        }

                    });
                }else{
                    res.send(dataBerita);
                }
            }else{
                console.log("Kategori : " +req.query.kategori.toLowerCase() );
                res.send(kategori[req.query.kategori.toLowerCase()].data);
            }
        } catch (error) {
            console.error("Error fetching news:", error);
            res.status(500).send("Internal Server Error");
        }
    } 

    addBerita(data,callback){

        console.log(data);
        beritas.select(" SUBSTR(berita_id,2) as newID ").customSelectQuery(" ORDER BY berita_id DESC LIMIT 1").get((result)=>{
            let newID = JSON.parse(JSON.stringify(result))[0].newID;
            newID = "000000"+(parseInt(newID)+1).toString();
            newID = "B" + newID.slice(-6);
            beritas.create({
                "berita_id" : newID,
                "gambar" : data['gambar'],
                "deskripsi" : data['deskripsi'],
                "author" : data['author'],
                "url" : data['url'],
                "waktu" : data['waktu'],
                "judul" : data['judul'],
                "sumber" : data['sumber'],
                "kategori" : data['kategori']
            }, (result)=>{
                let message = "berhasil menambahkan berita !"
                let status = "sukses"
                if(result.status == "error"){
                    status = "error";
                    message = result.message
                }
                return callback({
                    "status" : status,
                    "message" : message,
                    "newID" : newID
                });
            });    
            
        });
    }

    getBeritabyCategory(res,req){
        res.send({
            "kategori" : req.query.kategori
        });
    }

}


module.exports = beritaController;