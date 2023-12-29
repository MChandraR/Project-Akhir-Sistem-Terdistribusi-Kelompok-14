const model = require("./model");

class user extends model{
    constructor(){ 
        super();
        this.setTable('user');
        this.setFillable(['user_id','username','password','email','role']); //isi kolom yang bisa diedit dalam tabel
    }
}

module.exports = user;