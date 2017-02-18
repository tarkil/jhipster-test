import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CardTypeComponent } from './card-type.component';
import { CardTypeDetailComponent } from './card-type-detail.component';
import { CardTypePopupComponent } from './card-type-dialog.component';
import { CardTypeDeletePopupComponent } from './card-type-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CardTypeResolvePagingParams implements Resolve<any> {

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

export const cardTypeRoute: Routes = [
  {
    path: 'card-type',
    component: CardTypeComponent,
    resolve: {
      'pagingParams': CardTypeResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.cardType.home.title'
    }
  }, {
    path: 'card-type/:id',
    component: CardTypeDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.cardType.home.title'
    }
  }
];

export const cardTypePopupRoute: Routes = [
  {
    path: 'card-type-new',
    component: CardTypePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.cardType.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'card-type/:id/edit',
    component: CardTypePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.cardType.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'card-type/:id/delete',
    component: CardTypeDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.cardType.home.title'
    },
    outlet: 'popup'
  }
];
