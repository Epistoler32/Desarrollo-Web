window.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("productForm");
  if (!form) return;

  const submitBtn = document.getElementById("submitBtn");
  const idField = form.querySelector('input[name="id"]');

  if (idField && idField.value) {
    submitBtn.textContent = "Actualizar";
  } else {
    submitBtn.textContent = "Guardar";
  }

  // live preview of image URL if provided
  const imageUrlInput = document.getElementById("imageUrl");
  if (imageUrlInput) {
    const preview = document.createElement("img");
    preview.style.maxWidth = "100%";
    preview.style.marginTop = "10px";
    imageUrlInput.parentNode.appendChild(preview);

    const updatePreview = () => {
      const url = imageUrlInput.value.trim();
      if (url) {
        preview.src = url;
      } else {
        preview.removeAttribute("src");
      }
    };
    imageUrlInput.addEventListener("input", updatePreview);
    updatePreview();
  }
});
