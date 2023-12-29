const model = require("./model");

class bookmark extends model{
    constructor(){ 
        super();
        this.setTable('bookmark');
        this.setFillable(['berita_id','user_id','waktu','mardk_id']); //isi kolom yang bisa diedit dalam tabel
    }
}

module.exports = bookmark;