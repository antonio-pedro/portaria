import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'om',
    data: { pageTitle: 'portariaApp.oM.home.title' },
    loadChildren: () => import('./om/om.routes'),
  },
  {
    path: 'posto-graduacao',
    data: { pageTitle: 'portariaApp.postoGraduacao.home.title' },
    loadChildren: () => import('./posto-graduacao/posto-graduacao.routes'),
  },
  {
    path: 'militar',
    data: { pageTitle: 'portariaApp.militar.home.title' },
    loadChildren: () => import('./militar/militar.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
