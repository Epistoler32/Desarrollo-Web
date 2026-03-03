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