/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function showImg(obj) {
    var img_src = obj.getAttribute('data-preview');
    document.getElementById("image").style.display = "block";
    document.getElementById("image").src = img_src.toString();
    obj.style.background = "#F0E68C"; 
    obj.style.cursor = "pointer";
}

function hideImg(obj) {
    document.getElementById("image").style.display = "none";
    obj.style.background = "white"; 
}

