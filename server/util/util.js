String.prototype.replaceAt = function(index, replacement) {
    return this.substring(0, index) + replacement + this.substring(index + replacement.length);
}

class util{
    sendResponse(res,status,message,data=null){
        res.send({
            "status" : status,
            "message" : message,
            "data" : data,
        });  
    }  

    createToken(){
        let token = "" ;
        for(let i=0;i<50;i++){
            let num  = Math.floor(Math.random()*150);
            let temp = num;
            if(num<=57) num = 48+ num%10;
            else if(num<=90) num = 65 +  num%26;
            else num = 97 +  num%26;
            token += String.fromCharCode(num);
        }
        return token;
    }

    encodeTime(time){
        var newTime = "";
        for(let i=0;i<time.length;i++){
            newTime += time[i]=="T" || time[i]=="Z" ? " " : time[i];
        }
        return newTime
    }

  
}

module.exports = util;

 