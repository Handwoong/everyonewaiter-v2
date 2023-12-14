<svelte:head>
	<title>로그인 | 모두의 웨이터</title>
</svelte:head>

<script lang="ts">
	import FormInput from '$components/FormInput.svelte';
</script>

<form action="index.html" method="GET">
	<FormInput id="email" label="이메일" type="email" />
	<FormInput id="password" label="비밀번호" type="password" />
	<div class="login-buttons">
		<button class="btn btn-theme h-45px d-block w-100 btn-lg" type="submit">로그인</button>
	</div>
</form>
<br />
<a href="/accounts/register">아직 회원이 아니신가요?</a>
