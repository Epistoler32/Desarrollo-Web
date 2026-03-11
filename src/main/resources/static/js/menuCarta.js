// ─── menuCarta.js ─────────────────────────────────────────────────────────────
// Lógica exclusiva de menu_carta y product_detail.

// ─── Reveal on scroll para tarjetas de producto ───────────────────────────────
const revealEls = document.querySelectorAll('.product-card');

const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry, i) => {
    if (entry.isIntersecting) {
      const delay = (i % 3) * 80;
      entry.target.style.animationDelay = delay + 'ms';
      entry.target.classList.add('visible');
      observer.unobserve(entry.target);
    }
  });
}, { threshold: 0.12 });

revealEls.forEach((el) => observer.observe(el));

// ─── Botón Agregar (feedback visual) ─────────────────────────────────────────
document.querySelectorAll('.btn-agregar').forEach(btn => {
  btn.addEventListener('click', function () {
    const original = this.textContent;
    this.textContent = '✓ Agregado';
    this.style.background = 'linear-gradient(135deg, #400101, #730202)';
    this.style.color = '#F2B705';
    setTimeout(() => {
      this.textContent = original;
      this.style.background = '';
      this.style.color = '';
      const href = this.getAttribute('href');
      if (href) {
        window.location.href = href;
      }
    }, 600);
  });
});