import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OMComponent } from './list/om.component';
import { OMDetailComponent } from './detail/om-detail.component';
import { OMUpdateComponent } from './update/om-update.component';
import OMResolve from './route/om-routing-resolve.service';

const oMRoute: Routes = [
  {
    path: '',
    component: OMComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OMDetailComponent,
    resolve: {
      oM: OMResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OMUpdateComponent,
    resolve: {
      oM: OMResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OMUpdateComponent,
    resolve: {
      oM: OMResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default oMRoute;
