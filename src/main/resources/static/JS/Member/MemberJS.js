//아이디 찾기
function findid(){
    var memail = $("#memail").val();
    var mpw = $("#mpw").val();
    $.ajax({
        url:"/Member/FindIdController",
        method: "post",
        data:{"memail":memail, "mpw":mpw},
        success: function(result) {
            $("#findidmsg").html("회원님의 아이디는 "+result+"입니다.");
            $("#findidmsg").css({"color":"#ff7f50","font-size":"1.0em"});
        }
    });
}