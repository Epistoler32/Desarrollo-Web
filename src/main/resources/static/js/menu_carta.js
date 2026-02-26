const navbar = document.getElementById('navbar');
const navLinks = document.querySelectorAll('.page-link');
const loginLink = document.querySelector('.login-link');

function updateNavbar() {
  const scrollY = window.scrollY;
  const threshold = 300;
  const progress = Math.min(scrollY / threshold, 1);

  // Actualizar fondo de navbar
  const bgOpacity = progress * 1;
  const blurAmount = progress * 12;
  const shadowAlpha = progress * 0.15;

  navbar.style.background = `linear-gradient(
    to bottom,
    rgba(217, 61, 4, ${bgOpacity}),
    rgba(217, 121, 4, ${bgOpacity})
  )`;
  navbar.style.backdropFilter = `blur(${blurAmount}px)`;
  navbar.style.webkitBackdropFilter = `blur(${blurAmount}px)`;
  navbar.style.boxShadow = shadowAlpha > 0
    ? `0 4px 20px rgba(217, 61, 4, ${shadowAlpha})`
    : 'none';

  if (progress >= 0.1) {
    navLinks.forEach(link => {
      link.classList.add('scrolled');
    });
    if (loginLink) {
      loginLink.classList.add('scrolled');
    }
  } else {
    navLinks.forEach(link => {
      link.classList.remove('scrolled');
    });
    if (loginLink) {
      loginLink.classList.remove('scrolled');
    }
  }
}

// Event listeners
window.addEventListener('scroll', updateNavbar, { passive: true });
updateNavbar();




// ─── Reveal on scroll ───────────────────────────────────
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

revealEls.forEach(el => observer.observe(el));

// ─── Botón Agregar (por ahora solo esta visual) ─────────────────────
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