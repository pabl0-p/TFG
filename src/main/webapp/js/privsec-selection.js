var encryption = document.getElementById('encryption');
var view = document.getElementById('view_access');
var edit = document.getElementById('edit');

function updateRequired(){
    if ( view.value !== "" || edit.value !== ""){
        encryption.required = true;
        view.required = true;
        edit.required = true;
    } else{
        encryption.required = false;
        view.required = false;
        edit.required = false;
    }
}

view.addEventListener('change', updateRequired);
edit.addEventListener('change', updateRequired);
