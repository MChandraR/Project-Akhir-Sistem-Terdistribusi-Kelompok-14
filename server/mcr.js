let param = process.argv;
const input = require('input');
const async = require('rxjs');
let model = "" ;


async function create(type,namespace){
    const fs = require('fs');
    const inputFile = type=="model" ? './util/modelBase.txt' :'./util/controllerBase.txt' ;

    fs.readFile(inputFile, 'utf8', async (err, data) => {
    if (err) {
        console.error('Error reading file:', err);
        return;
    }

    if(type=="controller"){
        console.log("Tambahkan model ? (y/n) ");
        let inp = await input.text(":");
        model = (inp=="y") ? await input.text("\nMasukkan model : ") : "model";
        data = 
        "const "+model+" = require('../model/"+model+"'); \nconst "+model+"s = new "+model+"();" +
        data;
    }

    data = data.replace("namespace",namespace);
    data = data.replace("'namespace'","'"+namespace+"'");
    data = data.replace("namespace",namespace);
    const editedContent = data;
    const outputFile = type=="model" ? './model/'+namespace+'.js' : './controller/'+namespace+'.js';

    fs.writeFile(outputFile, editedContent, 'utf8', (err) => {
        if (err) {
        console.error('Error writing file:', err);
        return;
        }

        console.log(`File ${outputFile} telah disimpan.`);
    });
    });
}


if(param[2].includes('create')){
    if(param[2].includes('model')){
        let nameSpace = param[3];
        create('model',nameSpace);
    }else if(param[2].includes('controller')){
        let nameSpace = param[3];
        create('controller',nameSpace);

    }
}
