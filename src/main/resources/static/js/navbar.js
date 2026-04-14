// ─── navbar.js ────────────────────────────────────────────────────────────────
// Efecto de scroll compartido para todas las páginas públicas.

const navbar = document.getElementById('navbar');
const navLinks = document.querySelectorAll('.page-link');
const loginLink = document.querySelector('.login-link');

function updateNavbar() {
  const scrollY = window.scrollY;
  const threshold = 300;
  const progress = Math.min(scrollY / threshold, 1);

  const blurAmount = progress * 14;
  const shadowAlpha = progress * 0.12;

  // Gradiente cálido suave -> tono más claro y ligeramente más transparente
  navbar.style.background = `linear-gradient(
    to bottom,
    rgba(225, 110, 45, ${progress * 0.78}),
    rgba(230, 160, 55, ${progress * 0.78})
  )`;
  navbar.style.backdropFilter = `blur(${blurAmount}px)`;
  navbar.style.webkitBackdropFilter = `blur(${blurAmount}px)`;
  navbar.style.boxShadow = shadowAlpha > 0
    ? `0 2px 16px rgba(64, 1, 1, ${shadowAlpha})`
    : 'none';

  if (progress >= 0.1) {
    navLinks.forEach(link => link.classList.add('scrolled'));
    if (loginLink) loginLink.classList.add('scrolled');
  } else {
    navLinks.forEach(link => link.classList.remove('scrolled'));
    if (loginLink) loginLink.classList.remove('scrolled');
  }
}

window.addEventListener('scroll', updateNavbar, { passive: true });
updateNavbar();