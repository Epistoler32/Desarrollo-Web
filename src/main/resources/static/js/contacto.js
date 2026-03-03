// ─── Navbar scroll effect (igual que menuCarta.js) ──────────────
    const navbar = document.getElementById('navbar');
    const navLinks = document.querySelectorAll('.page-link');

    function updateNavbar() {
      const scrollY = window.scrollY;
      const threshold = 300;
      const progress = Math.min(scrollY / threshold, 1);

      const bgOpacity  = progress * 1;
      const blurAmount = progress * 12;
      const shadowAlpha = progress * 0.15;

      navbar.style.background = `linear-gradient(to bottom, rgba(217,61,4,${bgOpacity}), rgba(217,121,4,${bgOpacity}))`;
      navbar.style.backdropFilter = `blur(${blurAmount}px)`;
      navbar.style.webkitBackdropFilter = `blur(${blurAmount}px)`;
      navbar.style.boxShadow = shadowAlpha > 0
        ? `0 4px 20px rgba(217,61,4,${shadowAlpha})`
        : 'none';

      navLinks.forEach(link => {
        if (progress >= 0.1) {
          link.classList.add('scrolled');
        } else {
          link.classList.remove('scrolled');
        }
      });
    }

    window.addEventListener('scroll', updateNavbar, { passive: true });
    updateNavbar();

    // ─── Reveal on scroll ──────────────────────────────────────────
    const revealEls = document.querySelectorAll('.reveal');

    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry, i) => {
        if (entry.isIntersecting) {
          const delay = (Array.from(revealEls).indexOf(entry.target) % 4) * 100;
          entry.target.style.animationDelay = delay + 'ms';
          entry.target.classList.add('visible');
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.12 });

    revealEls.forEach(el => observer.observe(el));

    // ─── Botón submit (feedback visual) ────────────────────────────
    document.querySelector('form').addEventListener('submit', function(e) {
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