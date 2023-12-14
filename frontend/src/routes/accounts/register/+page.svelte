<svelte:head>
	<title>회원가입 | 모두의 웨이터</title>
</svelte:head>

<script lang="ts">
	import FormInput from '$components/FormInput.svelte';
</script>

<form action="index.html" method="GET">
	<FormInput id="email" label="email@example.com" type="email" />
	<FormInput id="password" label="특수문자, 영문, 숫자 두가지 이상 조합 8자리 이상" type="password" />
	<FormInput id="confirm-password" label="비밀번호 확인" type="password" />
	<div class="login-buttons">
		<button class="btn btn-theme h-45px d-block w-100 btn-lg" type="submit">가입하기</button>
	</div>
</form>
<br />
<a href="/accounts/login">이미 회원이신가요?</a>
