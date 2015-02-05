function test(){

    alert("hello");
}


    var i = 0;
     function changeColor(){
   var color = new Array("red","orange","yellow","green","blue","indigo","purple");
   var message = new Array("红","橙","黄","绿","蓝","靛","紫");
   document.getElementById("color").style.color = color[i];
   document.getElementById("message").innerHTML = message[i];
   i++;
   if(i==color.length){
    i = 0;
   }
  }