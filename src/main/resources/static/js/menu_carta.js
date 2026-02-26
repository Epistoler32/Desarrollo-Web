// ─── Navbar con fondo sólido al hacer scroll ────────────────
const navbar = document.getElementById('navbar');
window.addEventListener('scroll', () => {
  if (window.scrollY > 60) {
    navbar.classList.add('scrolled');
  } else {
    navbar.classList.remove('scrolled');
  }
});

// ─── Reveal on scroll ───────────
const revealEls = document.querySelectorAll('.product-card');

const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry, i) => {
    if (entry.isIntersecting) {
      const delay = (i % 3) * 80; // máx 3 columnas
      entry.target.style.animationDelay = delay + 'ms';
      entry.target.classList.add('visible');
      observer.unobserve(entry.target);
    }
  });
}, { threshold: 0.12 });

revealEls.forEach(el => observer.observe(el));

// ─── Botón Agregar (por ahora solo esta visual) ──────────────────────
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
    }, 1200);
  });
});