import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Game } from './game.model';
import { GameService } from './game.service';

@Component({
    selector: 'jhi-game-detail',
    templateUrl: './game-detail.component.html'
})
export class GameDetailComponent implements OnInit, OnDestroy {

    game: Game;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private gameService: GameService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['game']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.gameService.find(id).subscribe(game => {
            this.game = game;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
