/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {                      
            document.getElementById('pre-img').setAttribute('src', e.target.result);
            document.getElementById('pre-img').style.display = "block";
        }

        reader.readAsDataURL(input.files[0]);
    }
}
document.getElementById('imgInp').addEventListener('change', function () {
    readURL(this);
});
