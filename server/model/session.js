const model = require("./model");

class session extends model{
    constructor(){ 
        super();
        this.setTable('session');
        this.setFillable(['session_id','user_id','created_at','token']); //isi kolom yang bisa diedit dalam tabel
    }
}

module.exports = session;