const model = require("./model");

class berita extends model{
    constructor(){ 
        super();
        this.setTable('berita');
        this.setFillable(
            [
                'berita_id',
                'gambar',
                'deskripsi',
                'author',
                'url',
                'waktu',
                'judul',
                'sumber',
                'kategori'
            ]
        ); //isi kolom yang bisa diedit dalam tabel
    }
}

module.exports = berita;