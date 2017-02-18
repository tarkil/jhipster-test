import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ReprintComponent } from './reprint.component';
import { ReprintDetailComponent } from './reprint-detail.component';
import { ReprintPopupComponent } from './reprint-dialog.component';
import { ReprintDeletePopupComponent } from './reprint-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ReprintResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const reprintRoute: Routes = [
  {
    path: 'reprint',
    component: ReprintComponent,
    resolve: {
      'pagingParams': ReprintResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.reprint.home.title'
    }
  }, {
    path: 'reprint/:id',
    component: ReprintDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.reprint.home.title'
    }
  }
];

export const reprintPopupRoute: Routes = [
  {
    path: 'reprint-new',
    component: ReprintPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.reprint.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'reprint/:id/edit',
    component: ReprintPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.reprint.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'reprint/:id/delete',
    component: ReprintDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.reprint.home.title'
    },
    outlet: 'popup'
  }
];
