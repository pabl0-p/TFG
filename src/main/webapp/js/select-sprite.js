/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var img = document.getElementById("img");

img.addEventListener("click", function (e) {
    var original_width  = img.naturalWidth;
    var original_height = img.naturalHeight;
    var image = img.getBoundingClientRect();

    var click_X = e.clientX - image.left;
    var click_Y = e.clientY - image.top;
    
    click_X = Math.max(0, click_X);
    click_Y = Math.max(0, click_Y);

    var result_x = Math.max(0,Math.trunc(click_X*original_width/img.width));
    var result_y = Math.max(0,Math.trunc(click_Y*original_height/img.height));

    document.getElementById("output_x").innerHTML = result_x + ' px';
    document.getElementById("output_y").innerHTML = result_y + ' px';
    document.getElementById("sprite_x").value = 
            Math.trunc(result_x*100/original_width);
    document.getElementById("sprite_y").value = 
            Math.trunc(result_y*100/original_height);
    document.getElementById("sprite").style.left = e.clientX +  
            document.body.scrollLeft + 
            document.documentElement.scrollLeft + 'px';
    document.getElementById("sprite").style.top = e.clientY +  
            document.body.scrollTop + 
            document.documentElement.scrollTop + 'px';
    document.getElementById("sprite").style.display = "block";
    document.getElementById("selectSprite_button").style.pointerEvents = "auto";
});

function getImgSize() {
    var original_width  = img.naturalWidth;
    var original_height = img.naturalHeight;
    document.getElementById("img_width").innerHTML = original_width + ' px';
    document.getElementById("img_height").innerHTML = original_height + ' px';
}

