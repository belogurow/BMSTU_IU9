(define (my-range a b d)
  (if (< a b)
      (cons a (my-range (+ a d) b d))
      '()))
  
(define (my-flatten xs)
  (if (not (null? xs))      
      (if (pair? (car xs))
          (append (my-flatten (car xs)) (my-flatten (cdr xs)))
          (cons (car xs) (my-flatten (cdr xs))))
      '()))

(define (my-element? x xs)
  (if (not (null? xs))
      (if (equal? x (car xs))
          (equal? x (car xs))
          (my-element? x (cdr xs)))
      (not (null? xs))))

(define (my-filter pred? xs)
  (if (null? xs)
      '()
      (if (pred? (car xs))
          (cons (car xs) (my-filter pred? (cdr xs)))
          (my-filter pred? (cdr xs)))))

(define (my-fold-left op xs)
  (let ((start (car xs)))
    (if (null? (cdr xs))
        start
        (my-fold-left op (cons (op start (car (cdr xs))) (cdr (cdr xs)))))))

(define (m-f-right op xs)
  (let ((start (car xs)))
    (if (null? (cdr xs))
        start
        (m-f-right op (cons (op (car (cdr xs)) start) (cdr (cdr xs)))))))
               

(define (my-fold-right op xs)
  (m-f-right op (reverse xs))) 
