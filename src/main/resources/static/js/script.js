//console.log("This is External Javascript");

const toggleSidebar = ()=>{

 if($(".sidebar").is(":visible")){
    
	$(".sidebar").css("display","none");
    $(".content").css("margin-left","0%");

 }else{
    $(".sidebar").css("display","block");
    $(".content").css("margin-left","20%");
  }
}

// search Contact
// write modern js function

const searchContact= ()=>{

  //console.log("searching");

let query =$("#searchInput").val();
console.log(query);

if(query=="")
{
$(".search-result").hide();
}else{
//search
console.log(query);

//send request to server
let url = `http://192.168.43.195:8080/SmartContactManager/user/search/${query}`;
//let url = `http://192.168.1.8:8080/SmartContactManager/user/search/${query}`;

fetch(url).then((response) => {
  return response.json();
}).then((data) => {
//data

//console.log(data);

let text = `<div class='list-group'>`;

data.forEach(contact => {
  text += `<a href='/SmartContactManager/user/${contact.contactId}/contact'
   class ='list-group-item list-group-item-action'>${contact.firstName+' '+contact.lastName}</a>`;
});
text+=`</div>`;

$(".search-result").html(text);

$(".search-result").show();

});

}
}