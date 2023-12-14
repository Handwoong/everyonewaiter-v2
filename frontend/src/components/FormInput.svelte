<script lang="ts">
	export let id: string;
	export let label: string;
	export let type: string = 'text';
</script>

<div class="form-floating mb-20px">
	<input class="form-control fs-13px h-45px" {id} placeholder="{label}" {type} />
	<label class="d-flex align-items-center" for="{id}">{label}</label>
</div>
