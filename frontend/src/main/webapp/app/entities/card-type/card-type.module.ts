import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FrontendSharedModule } from '../../shared';

import {
    CardTypeService,
    CardTypePopupService,
    CardTypeComponent,
    CardTypeDetailComponent,
    CardTypeDialogComponent,
    CardTypePopupComponent,
    CardTypeDeletePopupComponent,
    CardTypeDeleteDialogComponent,
    cardTypeRoute,
    cardTypePopupRoute,
    CardTypeResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...cardTypeRoute,
    ...cardTypePopupRoute,
];

@NgModule({
    imports: [
        FrontendSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CardTypeComponent,
        CardTypeDetailComponent,
        CardTypeDialogComponent,
        CardTypeDeleteDialogComponent,
        CardTypePopupComponent,
        CardTypeDeletePopupComponent,
    ],
    entryComponents: [
        CardTypeComponent,
        CardTypeDialogComponent,
        CardTypePopupComponent,
        CardTypeDeleteDialogComponent,
        CardTypeDeletePopupComponent,
    ],
    providers: [
        CardTypeService,
        CardTypePopupService,
        CardTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FrontendCardTypeModule {}
