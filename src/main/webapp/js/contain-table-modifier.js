/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function showImg(obj, possible) {
    var img_src = obj.getAttribute('data-value');
    document.getElementById("pre-img").style.display = "none";
    document.getElementById("image").style.display = "block";
    document.getElementById("image").src = img_src.toString();
    if (possible) {
        obj.style.background = "#F0E68C"; 
        obj.style.cursor = "pointer";
    }
    // if(extra) {
    //     var img_src_r = obj.getAttribute('data-extra');
    //     var img_replace = document.getElementById("img-replace");
    //     var img = document.getElementById("image");

    //     img_replace.style.display = "block";
    //     img_replace.src = img_src_r.toString();

    //     img.style.maxHeight = "30vh";
    //     img.style.maxWidth = "35vw";
    //     img.style.top = "35%";
    //     img.style.left = "75%";
    //     img.style.transform = "translate(-50%, -50%)";

    //     img_replace.style.maxHeight = "35vh";
    //     img_replace.style.mazWidth = "35vw";
    //     img_replace.style.top = "70%";
    //     img_replace.style.left = "75%";
    //     img_replace.style.transform = "translate(-50%, -50%)";
    // }
}

function hideImg(obj, possible, extra) {
    
    document.getElementById("image").style.display = "none";
    
    if(document.getElementById("pre-img").getAttribute('src').toString() !== ''){
       document.getElementById("pre-img").style.display = "block";
    }
    if (possible) {
        obj.style.background = "white"; 
    }
    // if(extra) {
    //     document.getElementById("img-replace").style.display = "none";
    //     var img_replace = getElementById("img-replace");
    //     var img = getElementById("image");

    //     img.style.maxHeight = "";
    //     img.style.mazWidth = "";
    //     img.style.top = "";
    //     img.style.left = "";
    //     img.style.transform = "";

    //     img_replace.style.maxHeight = "";
    //     img_replace.style.mazWidth = "";
    //     img_replace.style.top = "";
    //     img_replace.style.left = "";
    //     img_replace.style.transform = "";
    // }
}
