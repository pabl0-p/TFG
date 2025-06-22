var img = document.getElementById("img");
var roi = document.getElementById("roi")
var isSelecting = false;

var start_X = 0;
var start_Y = 0;

img.draggable = false;

function getImageOffset(){
    var image = img.getBoundingClientRect();
    var container = img.parentElement.getBoundingClientRect();
    
    return {
        left: image.left - container.left,
        top: image.top - container.top
    };
} 

img.addEventListener("mousedown", function (e) {
    e.preventDefault();
    var image = img.getBoundingClientRect();
    var offset = getImageOffset();

    start_X = e.clientX - image.left;
    start_Y = e.clientY - image.top;
    isSelecting = true;

    roi.style.display = "block";
    roi.style.left = (start_X + offset.left) + "px";
    roi.style.top = (start_Y + offset.top) + "px";
    roi.style.width = "0px";
    roi.style.height = "0px";

    img.addEventListener('mousemove', mouseMove)
});
    

function mouseMove(ev) {
    if (!isSelecting) return;
    var image = img.getBoundingClientRect();
    var offset = getImageOffset();
    
    var coords_X = ev.clientX - image.left;
    var coords_Y = ev.clientY - image.top;

    var roi_X = Math.min(coords_X, start_X)
    var roi_Y = Math.min(coords_Y, start_Y)
    var roi_width = Math.abs(coords_X - start_X);
    var roi_height = Math.abs(coords_Y - start_Y);

    roi.style.left = (roi_X + offset.left) + "px";
    roi.style.top = (roi_Y + offset.top) + "px";
    roi.style.width = roi_width + "px";
    roi.style.height = roi_height + "px";
}

document.addEventListener("mouseup", function(e) {
    if (!isSelecting) return;
    isSelecting = false;
    img.removeEventListener("mousemove", mouseMove);

    var image = img.getBoundingClientRect();
    var final_X = e.clientX - image.left;
    var final_Y = e.clientY - image.top;

    final_X = Math.max(0, Math.min(final_X, img.width));
    final_Y = Math.max(0, Math.min(final_Y, img.height));

    var roi_X = Math.min(final_X, start_X)
    var roi_Y = Math.min(final_Y, start_Y)
    var roi_width = Math.abs(final_X - start_X);
    var roi_height = Math.abs(final_Y - start_Y);

    var original_width  = img.naturalWidth;
    var original_height = img.naturalHeight;

    var real_roi_X = Math.max(0,Math.trunc(roi_X*original_width/img.width));
    var real_roi_Y = Math.max(0,Math.trunc(roi_Y*original_height/img.height));
    var real_roi_width = Math.max(0, Math.trunc(roi_width*original_width/img.width));
    var real_roi_height = Math.max(0,Math.trunc(roi_height*original_height/img.height));
    
    console.log(document.getElementById("roi_x_display"));
    console.log(document.getElementById("roi_y_display"));
    console.log(document.getElementById("roi_width_display"));
    console.log(document.getElementById("roi_height_display"));

    document.getElementById("roi_x_display").innerHTML = real_roi_X + ' px';
    document.getElementById("roi_y_display").innerHTML = real_roi_Y + ' px';
    document.getElementById("roi_width_display").innerHTML = real_roi_width + ' px';
    document.getElementById("roi_height_display").innerHTML = real_roi_height + ' px';

    console.log(document.getElementById("roi_x"));
    console.log(document.getElementById("roi_y"));
    console.log(document.getElementById("roi_width"));
    console.log(document.getElementById("roi_height"));

    document.getElementById("roi_x").value = real_roi_X;
    document.getElementById("roi_y").value = real_roi_Y;
    document.getElementById("roi_width").value = real_roi_width;
    document.getElementById("roi_height").value = real_roi_height;
});

function getImgSize() {
    var original_width  = img.naturalWidth;
    var original_height = img.naturalHeight;
    document.getElementById("img_width").innerHTML = original_width + ' px';
    document.getElementById("img_height").innerHTML = original_height + ' px';
}
