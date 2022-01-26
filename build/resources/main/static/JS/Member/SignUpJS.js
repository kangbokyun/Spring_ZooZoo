function Signup() {
    var mid = $("#mid").val();
    var mpw = $("#mpw").val();
    var mname = $("#mname").val();
    var memail = $("#memail").val();
    var mbirth = $("#mbirth").val();
    var maddress = $("#maddress").val();

    $.ajax({
        url: "/Member/SignUpController",
        method: "post",
        data: {"mid" : mid, "mpw" : mpw, "mname" : mname, "memail" : memail, "mbirth" : mbirth, "maddress" : maddress},
        success: function(result) {
            if(result == 1) {
                alert("회원가입 성공");
                location.href = "/Member/Login";
            } else {
                alert("실패");
                location.reload();
            }
        }
    });
}