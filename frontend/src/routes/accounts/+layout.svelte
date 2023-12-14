<script>
	import Header from '$components/accounts/Header.svelte';
</script>

<div class="app" id="app">
	<div class="login login-v1">
		<div class="login-container">
			<Header />
			<div class="login-body">
				<div class="login-content fs-13px">
					<slot />
				</div>
			</div>
		</div>
	</div>
</div>
