import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { EditionComponent } from './edition.component';
import { EditionDetailComponent } from './edition-detail.component';
import { EditionPopupComponent } from './edition-dialog.component';
import { EditionDeletePopupComponent } from './edition-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class EditionResolvePagingParams implements Resolve<any> {

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

export const editionRoute: Routes = [
  {
    path: 'edition',
    component: EditionComponent,
    resolve: {
      'pagingParams': EditionResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.edition.home.title'
    }
  }, {
    path: 'edition/:id',
    component: EditionDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.edition.home.title'
    }
  }
];

export const editionPopupRoute: Routes = [
  {
    path: 'edition-new',
    component: EditionPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.edition.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'edition/:id/edit',
    component: EditionPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.edition.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'edition/:id/delete',
    component: EditionDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.edition.home.title'
    },
    outlet: 'popup'
  }
];
