// ─── contacto.js ──────────────────────────────────────────────────────────────
// Lógica exclusiva de la página de contacto.

// ─── Reveal on scroll ─────────────────────────────────────────────────────────
const revealEls = document.querySelectorAll('.reveal');

const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) {
      const delay = (Array.from(revealEls).indexOf(entry.target) % 4) * 100;
      entry.target.style.animationDelay = delay + 'ms';
      entry.target.classList.add('visible');
      observer.unobserve(entry.target);
    }
  });
}, { threshold: 0.12 });

revealEls.forEach(el => observer.observe(el));

// ─── Botón submit (feedback visual) ───────────────────────────────────────────
const form = document.querySelector('form');
if (form) {
  form.addEventListener('submit', function (e) {
    e.preventDefault();
    const btn = this.querySelector('button[type="submit"]');
    const original = btn.textContent;
    btn.textContent = '✓ Mensaje enviado';
    btn.style.background = 'linear-gradient(135deg, #1a4d00, #2d7a00)';
    setTimeout(() => {
      btn.textContent = original;
      btn.style.background = '';
    }, 3000);
  });
}