
const navbar = document.getElementById("navbar");

function updateNavbar() {
  const scrollY = window.scrollY;
  if (scrollY < 40) {
    // Completamente transparente - ARRIBA
    navbar.classList.remove("blurred", "scrolled");
  } else if (scrollY < 200) {
    // blur - MEDIO
    navbar.classList.add("blurred");
    navbar.classList.remove("scrolled");
  } else {
    // sólido con color - ABAJO
    navbar.classList.remove("blurred");
    navbar.classList.add("scrolled");
  }
}

window.addEventListener("scroll", updateNavbar, { passive: true });
updateNavbar(); // estado inicial

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
