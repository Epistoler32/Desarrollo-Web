(function () {
  const imageUrlInput = document.getElementById('imageUrl');
  const previewLabel = document.getElementById('previewLabel');
  if (!imageUrlInput || !previewLabel) return;

  function toggleLabel() {
    previewLabel.style.display = imageUrlInput.value.trim() ? 'block' : 'none';
  }

  imageUrlInput.addEventListener('input', toggleLabel);
  toggleLabel();
})();