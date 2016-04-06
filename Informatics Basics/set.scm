(define (my-element? x xs)
  (if (not (null? xs))
      (if (equal? x (car xs))
          (equal? x (car xs))
          (my-element? x (cdr xs)))
      (not (null? xs))))

(define (list->set xs)
  (if (null? xs)
      '()
      (if (my-element? (car xs) (cdr xs))
          (list->set (cdr xs))
          (cons (car xs) (list->set (cdr xs))))))

(define (set? xs)
  (equal? xs (list->set xs)))

(define (union xs ys)
  (list->set (append xs ys)))

(define (intersection xs ys)
  (if (null? xs)
      '()
      (if (my-element? (car xs) ys)
          (cons (car xs) (intersection (cdr xs) ys))
          (intersection (cdr xs) ys))))

(define (difference xs ys)
  (if (null? xs)
      '()
      (if (my-element? (car xs) ys)
          (difference (cdr xs) ys)
          (cons (car xs) (difference (cdr xs) ys)))))

(define (symmetric-difference xs ys)
  (union (difference xs ys) (difference ys xs)))

(define (set-eq? xs ys)
  (equal? (difference xs ys) '()))