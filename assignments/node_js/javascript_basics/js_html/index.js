let count = 0;

//Increase count by one
function increment(){
    count ++;
    document.getElementById('count').innerText = count;
}
function save(){
    let message = count + ' - ' ;
    //mdn for reference
    //use .textContent instead of .innerText
    //.innerText does not show content which is not human readable
    document.getElementById('save-el').textContent += message;
    //Reset the count to 0
    count = 0;
    //Update the text on the document
    document.getElementById('count').innerText = count;
}