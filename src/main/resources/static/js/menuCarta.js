// ─── menuCarta.js ─────────────────────────────────────────────────────────────

// ─── Reveal on scroll ─────────────────────────────────────────────────────────
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

// ─── FILTROS ───────────────────────────────────────────────────────────────────

const searchInput   = document.getElementById('filter-search');
const alergenoBtn   = document.getElementById('filter-alergenos');
const cards         = document.querySelectorAll('.product-card');
const categorySections = document.querySelectorAll('.category-section');

let soloSinAlergenos = false;

function applyFilters() {
  const query = searchInput ? searchInput.value.trim().toLowerCase() : '';

  cards.forEach(card => {
    const nombre    = (card.dataset.nombre  || '').toLowerCase();
    const alergenos = card.dataset.alergenos === 'true';

    const matchesSearch   = !query || nombre.includes(query);
    const matchesAlergeno = !soloSinAlergenos || !alergenos;

    card.style.display = (matchesSearch && matchesAlergeno) ? '' : 'none';
  });

  // Ocultar secciones enteras si todos sus productos están ocultos
  categorySections.forEach(section => {
    const visible = [...section.querySelectorAll('.product-card')]
      .some(c => c.style.display !== 'none');
    section.style.display = visible ? '' : 'none';
  });
}

if (searchInput) {
  searchInput.addEventListener('input', applyFilters);
}

if (alergenoBtn) {
  alergenoBtn.addEventListener('click', () => {
    soloSinAlergenos = !soloSinAlergenos;
    alergenoBtn.classList.toggle('active', soloSinAlergenos);
    alergenoBtn.setAttribute('aria-pressed', soloSinAlergenos);
    applyFilters();
  });
}