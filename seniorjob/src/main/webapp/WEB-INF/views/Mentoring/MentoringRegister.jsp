<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>멘토링 등록 페이지</title>
<style>
body {
  font-family: Arial, Helvetica, sans-serif;
  background-color: black;
}

* {
  box-sizing: border-box;
}

/* Add padding to containers */
.container {
  padding: 16px;
  background-color: white;
}

/* Full-width input fields */
input[type=text], input {
  width: 100%;
  padding: 15px;
  margin: 5px 0 22px 0;
  display: inline-block;
  border: none;
  background: #f1f1f1;
}

input[type=text]:focus, input:focus {
  background-color: #ddd;
  outline: none;
}

/* Overwrite default styles of hr */
hr {
  border: 1px solid #f1f1f1;
  margin-bottom: 25px;
}

/* Set a style for the submit button */
.registerbtn {
  background-color: #4CAF50;
  color: white;
  padding: 16px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  opacity: 0.9;
}

.registerbtn:hover {
  opacity: 1;
}

/* Add a blue text color to links */
a {
  color: dodgerblue;
}

/* Set a grey background color and center the text of the "sign in" section */
.signin {
  background-color: #f1f1f1;
  text-align: center;
}

#m_numOfDays{width:30%}
input[name=m_numOfDays]{font-size:large; color:red}
</style>
</head>
<body>

<form action="MentoringRegisterProc" method="post" onsubmit="mentoringCheck()">
  <div class="container">
    <h1>멘토링 등록</h1>
    <p>모든 빈칸을 채워주세요</p>
    <hr>
    
    <input type="hidden" name="mentor_id" value="${mentorInfo.mentor_id }">

    <label for="name"><b>멘토링 이름</b></label>
    <input type="text" placeholder="멘토링 이름" name="mentoring_name" id="name" required>
	
	<label for="date"><b>멘토링 시작 날짜</b></label>
	<input type="date" placeholder="Enter Date" name="mentoring_begin_date" id="mentoring_begin_date" required>
	    
	<label for="date"><b>멘토링 종료 날짜</b></label>
	<input type="date" placeholder="Enter Date" name="mentoring_end_date" id="mentoring_end_date" required>
	
	<div id="m_numOfDays">
		<label for="date"><b>총 멘토링 일수</b></label>
		<input type="text" placeholder="총 멘토링 일수" name="m_numOfDays" id="m_numOfDays" required> 일간 진행
	</div>

    <label for="psw-repeat"><b>정원</b></label>
    <input type="text" placeholder="멘토링 정원을 입력하세요" name="mentoring_limit" id="psw-repeat" required>
    <hr>

    <label for="psw"><b>내용</b></label>
    <textarea id="content" name="mentoring_content" rows="10" cols="90" style="height:200px; display:block; margin:15px; width:1345px"></textarea>
    
    <label for="psw"><b>멘토링 코스 이미지</b></label>
    <input type="file" placeholder="이미지를 등록하세요" name="mentoring_photo" id="psw" required>
    
    <label for="psw"><b>멘토링 금액</b></label>
    <input type="text" placeholder="금액을 입력하세요" name="mentoring_price" id="psw" required>
    
    <button type="submit" class="registerbtn">멘토링 등록하기</button>
    <button type="button" class="registerbtn" onclick="MentoringPreview()">미리보기</button>
  </div>
  
  <div class="container signin">
    <p><a href="getMain">Home</a>.</p>
  </div>
</form>
<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
<script>
	
	// 날짜 비교 함수 alert(시작날짜>종료날짜 && 시작날짜==종료날짜)
	// 멘토링 종료일 설정 완료 시 함수 실행(change)
	// 참고: https://mobilenweb.tistory.com/95
	$('#mentoring_end_date').change(function(){ 
		var s_date = $( "input[name='mentoring_begin_date']" ).val(); // 멘토링 시작일(2021-05-04 형태)
		var s_dateArr = s_date.split('-'); // -을 구분자로 연,월,일로 잘라내어 배열로 변환
		var e_date = $( "input[name='mentoring_end_date']" ).val(); // 멘토링 종료일
		var e_dateArr = e_date.split('-');
		
		// 배열에 담겨 있는 연[0],월[1],일[2]을 사용하여 Date 객체 생성
		var startDate = new Date(s_dateArr[0], s_dateArr[1], s_dateArr[2]);
		var endDate = new Date(e_dateArr[0], e_dateArr[1], e_dateArr[2]);
		var numOfDays = (endDate.getTime()-startDate.getTime()); // 멘토링 총 기간
		var cDay = 24*60*60*1000; // 시 * 분 * 초 * 밀리세컨
		
		// 날짜를 숫자형태의 날짜 정보로 변환하여 비교(getTime()활용)
		if(startDate.getTime() > endDate.getTime()){
			alert("종료날짜보다 시작날짜가 작아야 합니다.");
			$( "input[name='mentoring_begin_date']" ).val(''); // 해당 태그 값 초기화
			$( "input[name='mentoring_end_date']" ).val('');
			return false;
		}else if(startDate.getTime() == endDate.getTime()){
			alert("멘토링 시작일-종료일이 같습니다.\n멘토링 코스 최소 기간은 일주일(7일) 입니다.");
			$( "input[name='mentoring_begin_date']" ).val('');
			$( "input[name='mentoring_end_date']" ).val('');
			return false;
		}else if(numOfDays <= 7){
			alert("멘토링 코스 최소 기간은 일주일(7일) 입니다.\n기간을 재설정 해주세요.");
			$( "input[name='mentoring_begin_date']" ).val('');
			$( "input[name='mentoring_end_date']" ).val('');
			return false;
		}else{
			$('input[name=m_numOfDays]').attr('value',parseInt(numOfDays/cDay));
		}
	});

	// 미리보기
	function MentoringPreview(){
		alert("Mentoring Preview");
	}
	
	// 멘토링 등록 중복 체크
	function mentoringCheck(){
		var flag = true;
		$.ajax({
			url: "MentoringRegisterCheck",
			data: {"mentoring_begin_date" : $('#mentoring_begin_date').val()},		
			dataType: "json",
			success:function(result){
				if(result == 1){
					var msg = "해당 기간에 등록된 멘토링이 있습니다.";
					alert(msg);
					flag = false;
				}else{
					flag = true;
				}
			}
		});
		return flag;
	}
</script>

</body>
</html>