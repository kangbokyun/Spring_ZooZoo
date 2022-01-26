//아이디 찾기
function findid(){
    var mname = $("#mname").val();
    var memail = $("#memail").val();
    $.ajax({
        url:"/Member/FindIdController",
        method: "post",
        data:{"mname":mname, "memail":memail},
        success: function(result) {
            //1이면 공백값이라 공백막기
            if(result == 1){
                $("#findidmsg").html("내용을 입력해주세요.");
                $("#findidmsg").css({"color":"#ff7f50","font-size":"1.0em"});
            }else if(result == 2){
                $("#findidmsg").html("동일한 회원 정보가 없습니다.");
                $("#findidmsg").css({"color":"#ff7f50","font-size":"1.0em"});
            }else{
                $("#findidmsg").html("회원님의 아이디는 "+result+"입니다.");
                $("#findidmsg").css({"color":"#ff7f50","font-size":"1.0em"});
            }
        }
    });
}