/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function downloadFile(obj) {
    var file_url = obj.getAttribute('data-value');
    var file_name = obj.getAttribute('data-file');
    var a = document.createElement("a");
    a.href = file_url;
    a.setAttribute("download", file_name);
    a.click();
}