import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { CardType } from './card-type.model';
import { CardTypeService } from './card-type.service';

@Component({
    selector: 'jhi-card-type-detail',
    templateUrl: './card-type-detail.component.html'
})
export class CardTypeDetailComponent implements OnInit, OnDestroy {

    cardType: CardType;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private cardTypeService: CardTypeService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['cardType']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.cardTypeService.find(id).subscribe(cardType => {
            this.cardType = cardType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
