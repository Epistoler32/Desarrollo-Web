
const navbar = document.getElementById('navbar');

function updateNavbar() {
  const scrollY = window.scrollY;
  const threshold = 300; // píxeles hasta quedar 100% sólido

  // Progreso de 0 a 1 según cuánto se ha scrolleado
  const progress = Math.min(scrollY / threshold, 1);

  // Interpola cada valor
  const bgOpacity  = progress * 1;
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
}

window.addEventListener('scroll', updateNavbar, { passive: true });
updateNavbar();



// Efecto scroll reveal para galeria y comentarios
const revealEls = document.querySelectorAll(".gallery-figure, .comment-box");

const observer = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry, idx) => {
      if (entry.isIntersecting) {
        const delay = (Array.from(revealEls).indexOf(entry.target) % 3) * 120;
        entry.target.style.animationDelay = delay + "ms";
        entry.target.classList.add("visible");
        observer.unobserve(entry.target);
      }
    });
  },
  {
    threshold: 0.15,
    rootMargin: "0px 0px -60px 0px",
  },
);

revealEls.forEach((el) => observer.observe(el));

// Smooth scroll
document.querySelectorAll('a[href^="#"]').forEach((link) => {
  link.addEventListener("click", (e) => {
    const target = document.querySelector(link.getAttribute("href"));
    if (target) {
      e.preventDefault();
      target.scrollIntoView({ behavior: "smooth", block: "start" });
    }
  });
});
