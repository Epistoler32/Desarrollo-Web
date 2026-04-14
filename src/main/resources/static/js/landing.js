// ─── landing.js ───────────────────────────────────────────────────────────────
// Lógica exclusiva de la landing page.

// ─── Reveal on scroll para galería y comentarios ──────────────────────────────
const revealEls = document.querySelectorAll('.gallery-figure, .comment-box');

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const delay = (Array.from(revealEls).indexOf(entry.target) % 3) * 120;
        entry.target.style.animationDelay = delay + 'ms';
        entry.target.classList.add('visible');
        observer.unobserve(entry.target);
      }
    });
  },
  {
    threshold: 0.15,
    rootMargin: '0px 0px -60px 0px',
  }
);

revealEls.forEach((el) => observer.observe(el));

// ─── Smooth scroll para anclas internas ───────────────────────────────────────
document.querySelectorAll('a[href^="#"]').forEach((link) => {
  link.addEventListener('click', (e) => {
    const target = document.querySelector(link.getAttribute('href'));
    if (target) {
      e.preventDefault();
      target.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  });
});