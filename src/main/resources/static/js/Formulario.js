(function () {
  const imageUrlInput = document.getElementById('imageUrl');
  const previewLabel = document.getElementById('previewLabel');
  if (!imageUrlInput || !previewLabel) return;

  // Crear el elemento img de preview
  const preview = document.createElement('img');
  preview.style.maxWidth = '100%';
  preview.style.marginTop = '10px';
  preview.style.borderRadius = '8px';
  preview.style.display = 'none';
  imageUrlInput.parentNode.appendChild(preview);

  function updatePreview() {
    const url = imageUrlInput.value.trim();
    if (url) {
      preview.src = url;
      preview.style.display = 'block';
      previewLabel.style.display = 'block';
    } else {
      preview.style.display = 'none';
      previewLabel.style.display = 'none';
    }
  }

  imageUrlInput.addEventListener('input', updatePreview);

  // Mostrar preview si ya hay una URL al cargar (modo edición)
  updatePreview();
})();