import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FrontendBlockModule } from './block/block.module';
import { FrontendCardModule } from './card/card.module';
import { FrontendCardTypeModule } from './card-type/card-type.module';
import { FrontendEditionModule } from './edition/edition.module';
import { FrontendGameModule } from './game/game.module';
import { FrontendReprintModule } from './reprint/reprint.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        FrontendBlockModule,
        FrontendCardModule,
        FrontendCardTypeModule,
        FrontendEditionModule,
        FrontendGameModule,
        FrontendReprintModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FrontendEntityModule {}
