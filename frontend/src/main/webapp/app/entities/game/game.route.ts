import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { GameComponent } from './game.component';
import { GameDetailComponent } from './game-detail.component';
import { GamePopupComponent } from './game-dialog.component';
import { GameDeletePopupComponent } from './game-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class GameResolvePagingParams implements Resolve<any> {

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

export const gameRoute: Routes = [
  {
    path: 'game',
    component: GameComponent,
    resolve: {
      'pagingParams': GameResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.game.home.title'
    }
  }, {
    path: 'game/:id',
    component: GameDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.game.home.title'
    }
  }
];

export const gamePopupRoute: Routes = [
  {
    path: 'game-new',
    component: GamePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.game.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'game/:id/edit',
    component: GamePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.game.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'game/:id/delete',
    component: GameDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'frontendApp.game.home.title'
    },
    outlet: 'popup'
  }
];
