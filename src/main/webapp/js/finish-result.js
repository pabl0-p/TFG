/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function showImg(obj) {
    var img_src = obj.getAttribute('data-value');
    document.getElementById("final_image").style.display = "none";
    document.getElementById("image").style.display = "block";
    document.getElementById("image").src = img_src.toString();
    obj.style.background = "#F0E68C"; 
}

function hideImg(obj) {
    
    document.getElementById("image").style.display = "none";
    
    if(document.getElementById("final_image").getAttribute('src').toString() !== '#'){
       ;document.getElementById("final_image").style.display = "block";
    }
    obj.style.background = "white"; 
}
